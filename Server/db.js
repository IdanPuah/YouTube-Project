import mongoose from 'mongoose';

const connectDB = async () => {
    try {
        const mongoURI = 'mongodb://10.0.0.24:27017/test';
        const conn = await mongoose.connect(mongoURI);
        //mongodb://localhost:27017/
        //mongodb+srv://avivyair:Aa123123@cluster.5nhxrni.mongodb.net/?retryWrites=true&w=majority&appName=Cluster
        console.log(`MongoDB connected: ${conn.connection.host}`);
    } catch (error) {
        console.error(`Error: ${error.message}`);
        process.exit(1); // Exit process with failure
    }
};

mongoose.connection.on('connected', () => {
    console.log('Mongoose connected to DB');
});

mongoose.connection.on('error', (err) => {
    console.log('Mongoose connection error: ', err);
});

mongoose.connection.on('disconnected', () => {
    console.log('Mongoose disconnected from DB');
});

process.on('SIGINT', async () => {
    await mongoose.connection.close();
    console.log('Mongoose connection closed due to app termination');
    process.exit(0);
});

export default connectDB;